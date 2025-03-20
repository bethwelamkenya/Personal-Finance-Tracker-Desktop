package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import models.Screen
import models.User
import ui.components.ScrollBar
import ui.theme.LocalDimens
import viewModels.MainViewModel
import viewModels.ViewModelStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesktopLayout(
    viewModel: MainViewModel,
    content: @Composable () -> Unit
) {
    val dimens = LocalDimens.current
    val addScreens = listOf(
        Screen.AddBankAccount,
        Screen.AddSavingsGoal,
        Screen.AddTransaction
    )

// Make `containing` dynamically track `currentScreen`
    val currentScreen by viewModel.currentScreen
    val containing by remember(currentScreen) { derivedStateOf { addScreens.contains(currentScreen) } }

    var expand by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val scrollbarAdapter = rememberScrollbarAdapter(scrollState)

// Ensure `expandedMenu` updates dynamically
    val expandedMenu by remember(currentScreen, expand) {
        mutableStateOf(if (containing) true else expand)
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Sidebar Navigation
        Column(
            modifier = Modifier
                .width(dimens.sideBarWidth)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxHeight()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .height(dimens.sidebarHeaderHeight)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource("app_image.ico"),
                        contentDescription = "App Icon",
                        modifier = Modifier.size(dimens.iconSize)
                    )
                    Spacer(modifier = Modifier.width(dimens.smallPadding))
                    Text(
                        "Finance Tracker",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // Navigation Items
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(scrollState)
                        .padding(dimens.smallPadding),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimens.elevationLow)
                    ) {
                        listOf(
                            Screen.Dashboard,
                            Screen.Accounts,
                            Screen.SavingsGoals,
                            Screen.Transactions,
                            Screen.Budgets,
                            Screen.Reports,
                        ).forEach { screen ->
                            NavigationItem(
                                screen = screen,
                                isSelected = currentScreen == screen,
                                onSelect = { viewModel.navigateTo(screen) }
                            )
                        }
                        ExpandableNavigationItem(
                            screen = Screen.CreateNew,
                            items = addScreens,
                            selected = currentScreen,
                            expanded = expandedMenu,
                            onToggle = {
                                expand = it
                            },
                            onSelect = { viewModel.navigateTo(it) }
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimens.elevationLow),
//                        modifier = Modifier.height(dimens.sidebarFooterHeight)
                    ) {
                        NavigationItem(
                            screen = Screen.Profile,
                            isSelected = currentScreen == Screen.Profile,
                            onSelect = { viewModel.navigateTo(Screen.Profile) }
                        )
                        NavigationItemBottom(
                            screen = currentScreen,
                            onSelect = {
                                if (it == Screen.Logout) {
                                    viewModel.logout()
                                } else {
                                    viewModel.navigateTo(it)
                                }
                            }
                        )
                    }
                }
                ScrollBar(
                    adapter = scrollbarAdapter,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    changeColor = true
                )
            }
        }

        // Main Content Area
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            currentScreen.title,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    navigationIcon = if (viewModel.hasBack()) {
                        {
                            IconButton(onClick = {
                                viewModel.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    modifier = Modifier.size(dimens.iconSize)
                                )
                            }
                        }
                    } else {
                        { }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                content()
            }
        }
    }
}

@Composable
fun ExpandableNavigationItem(
    screen: Screen,
    items: List<Screen>,
    selected: Screen,
    expanded: Boolean,
    onToggle: (Boolean) -> Unit,
    onSelect: (Screen) -> Unit
) {
    val dimens = LocalDimens.current
    Column {
        NavigationItem(
            screen = screen,
            isSelected = false,
            onSelect = {
                onToggle(!expanded)
            },
            icon = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
        )

        if (expanded) {
            items.forEach { child ->
                NavigationItem(
                    screen = child,
                    isSelected = selected == child,
                    onSelect = {
                        onSelect(child)
                    },
                    modifier = Modifier.padding(start = dimens.largePadding)
                )
            }
        }
    }
}

@Composable
fun NavigationItem(
    modifier: Modifier = Modifier,
    screen: Screen,
    isSelected: Boolean,
    onSelect: () -> Unit,
    icon: ImageVector? = null
) {
    val dimens = LocalDimens.current
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.sideBarItemHeight)
            .hoverable(enabled = true, interactionSource = remember { MutableInteractionSource() }),
        color = backgroundColor,
        shape = MaterialTheme.shapes.medium,
        onClick = onSelect
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(dimens.mediumPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = screen.icon,
                    contentDescription = screen.title,
                    tint = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(dimens.largePadding)
                )
                Spacer(modifier = Modifier.width(dimens.mediumPadding))
                Text(
                    text = screen.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(dimens.largePadding)
                )
            }
        }
    }
}

@Composable
fun NavigationItemBottom(
    modifier: Modifier = Modifier,
    screen: Screen,
    onSelect: (Screen) -> Unit
) {
    val dimens = LocalDimens.current
    val backgroundColor = if (screen == Screen.Settings) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Surface(
            modifier = modifier
                .hoverable(enabled = true, interactionSource = remember { MutableInteractionSource() }),
            color = backgroundColor,
            shape = MaterialTheme.shapes.medium,
            onClick = { onSelect(Screen.Settings) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(dimens.mediumPadding)
            ) {
                Text(
                    text = Screen.Settings.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (screen == Screen.Settings) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Spacer(modifier = Modifier.width(dimens.mediumPadding))
                Icon(
                    imageVector = Screen.Settings.icon,
                    contentDescription = Screen.Settings.title,
                    tint = if (screen == Screen.Settings) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(dimens.largePadding)
                )
            }
        }

        Surface(
            modifier = modifier
                .hoverable(enabled = true, interactionSource = remember { MutableInteractionSource() }),
            color = Color.Transparent,
            shape = MaterialTheme.shapes.medium,
            onClick = { onSelect(Screen.Login) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(dimens.mediumPadding)
            ) {
                Text(
                    text = Screen.Logout.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (screen == Screen.Logout) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Spacer(modifier = Modifier.width(dimens.mediumPadding))
                Icon(
                    imageVector = Screen.Logout.icon,
                    contentDescription = Screen.Logout.title,
                    tint = if (screen == Screen.Logout) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(dimens.largePadding)
                )
            }
        }

    }
}

// Placeholder screen

@Composable
fun BudgetsScreen() {
    Text("Budgets Content", style = MaterialTheme.typography.headlineMedium)
}

@Composable
fun ReportsScreen() {
    Text("Reports Content", style = MaterialTheme.typography.headlineMedium)
}

@Preview
@Composable
fun DesktopPreview() {
    val viewModel = ViewModelStore.getViewModel<MainViewModel>()
    viewModel.setUser(
        User(
            name = "name",
            email = "email@email.email",
            passwordHash = "rdf6tdc5er"
        )
    )
    viewModel.refreshData()
    viewModel.navigateTo(Screen.Settings)
    DesktopLayout(
        viewModel = viewModel,
        content = {

        }
    )
}