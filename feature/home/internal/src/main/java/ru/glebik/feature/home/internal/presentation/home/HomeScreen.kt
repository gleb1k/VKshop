package ru.glebik.feature.home.internal.presentation.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowCircleLeft
import androidx.compose.material.icons.twotone.ArrowCircleRight
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import ru.glebik.core.designsystem.theme.AppTheme
import ru.glebik.core.navigation.SharedScreen
import ru.glebik.core.widget.ErrorView
import ru.glebik.core.widget.LoadingView
import ru.glebik.core.widget.NotFoundView
import ru.glebik.core.widget.R
import ru.glebik.core.widget.common.BaseSurface
import ru.glebik.core.widget.common.CustomCard
import ru.glebik.feature.home.api.model.response.products.Product
import ru.glebik.feature.home.internal.presentation.home.component.HomeAppBar
import ru.glebik.feature.home.internal.presentation.home.viewmodel.CategoryUi
import ru.glebik.feature.home.internal.presentation.home.viewmodel.HomeScreenModel
import ru.glebik.feature.home.internal.presentation.home.viewmodel.HomeStore

object HomeScreen : Screen {
    private fun readResolve(): Any = HomeScreen

    @Composable
    override fun Content() {
        HomeScreen()
    }


    @Composable
    private fun HomeScreen(
        viewModel: HomeScreenModel = getScreenModel()
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val state by viewModel.state.collectAsStateWithLifecycle()

        if (state.isLoading)
            LoadingView()
        else if (state.isError)
            ErrorView { viewModel.loadProducts(1) }
        else
            HomeView(
                state = state,
                navigator,
                viewModel
            )
    }

    @Composable
    private fun HomeView(
        state: HomeStore.State,
        navigator: Navigator,
        viewModel: HomeScreenModel
    ) {

        val openAlertDialog = remember { mutableStateOf(false) }

        BaseSurface {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {

                if (openAlertDialog.value)
                    FilterDialog(
                        state,
                        {
                            if (viewModel.isCategoryEnabled())
                                viewModel.loadProducts(1)
                            else
                                viewModel.loadProducts(state.currentPage)
                            openAlertDialog.value = false
                        },
                        viewModel::onChangeCategory
                    )


                HomeAppBar(
                    state,
                    viewModel::onQuerySearchChange,
                    viewModel::onSearchClick,
                    viewModel::isCategoryEnabled,
                    viewModel::enableSearch,
                    { openAlertDialog.value = true },
                )
                if (state.isNotFound) {
                    NotFoundView { viewModel.loadProducts(1) }
                }
                if (!state.isNotFound)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = AppTheme.padding.base
                        ),
                        verticalArrangement = Arrangement.spacedBy(AppTheme.padding.base),
                    ) {
                        items(state.products, key = { it.id }) {
                            ProductItem(
                                item = it,
                                navigator = navigator,
                            )
                        }
                    }
            }
            PaginationArrows(state, viewModel)
        }
    }

    @Composable
    private fun FilterDialog(
        state: HomeStore.State,
        onDismissRequest: () -> Unit,
        onChangeCategory: (CategoryUi) -> Unit,
    ) {
        Dialog(
            onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Categories",
                        modifier = Modifier.padding(16.dp),
                    )
                    LazyColumn(
                        modifier = Modifier
                            .height(400.dp)
                    ) {
                        items(state.categories) { category ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = category.title)
                                Checkbox(
                                    checked = category.isEnabled,
                                    onCheckedChange = {
                                        onChangeCategory(CategoryUi(category.title, it))
                                    })
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("OK")
                        }
                    }

                }
            }
        }
    }


    @Composable
    private fun PaginationArrows(
        state: HomeStore.State,
        viewModel: HomeScreenModel,
    ) {
        if (!state.isNotFound && !viewModel.isCategoryEnabled())
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { viewModel.loadProducts(state.currentPage - 1) }) {
                        Icon(
                            Icons.TwoTone.ArrowCircleLeft, "Left",
                            modifier = Modifier.fillMaxSize(),
                            tint = AppTheme.colors.primary
                        )
                    }
                    Spacer(modifier = Modifier.padding(vertical = AppTheme.padding.small))
                    Text(
                        text = state.currentPage.toString(),
                        style = AppTheme.typography.bodyBold,
                        color = AppTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.padding(vertical = AppTheme.padding.small))
                    IconButton(onClick = { viewModel.loadProducts(state.currentPage + 1) }) {
                        Icon(
                            Icons.TwoTone.ArrowCircleRight, "Right",
                            modifier = Modifier.fillMaxSize(),
                            tint = AppTheme.colors.primary
                        )
                    }
                }
            }
    }

    @Composable
    private fun ProductItem(
        item: Product,
        navigator: Navigator,
    ) {
        val detailScreen = rememberScreen(SharedScreen.Detail(id = item.id))
        val mContext = LocalContext.current

        CustomCard(
            onSmallClick = {
                navigator.push(detailScreen)
            },
            onLongClick = {
                Toast.makeText(mContext, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show()
            },
        ) {
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(mContext)
                        .data(item.thumbnail)
                        .crossfade(true)
                        .dispatcher(Dispatchers.IO)
                        .build(),
                    placeholder = painterResource(id = R.drawable.placeholder),
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(0.25f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppTheme.padding.base)
                        .weight(0.75f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${item.title}",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = AppTheme.typography.body
                    )
                    Text(
                        text = "${item.price} $",
                        overflow = TextOverflow.Ellipsis,
                        style = AppTheme.typography.body
                    )
                    Text(
                        text = item.description ?: "",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        style = AppTheme.typography.hint
                    )

                }
            }
        }
    }
}


