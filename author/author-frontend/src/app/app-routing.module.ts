import {RouterModule, Routes} from '@angular/router';
import {MainLayoutComponent} from './layout/main-layout/main-layout.component';
import {DashboardComponent} from './content/dashboard/dashboard.component';
import {BreadCrumbConfig} from './shared/utils/bread.crumb.config';
import {RouteType} from './shared/enums/route.type';
import {EmptyLayoutComponent} from './layout/empty-layout/empty-layout.component';
import {LoginComponent} from './pages/login/login.component';
import {NotFoundComponent} from './pages/not-found/not-found.component';
import {NgModule} from '@angular/core';
import {AuthGuardService} from './shared/service/auth-guard.service';
import {SitesComponent} from './content/sites/sites.component';
import {AddUpdateSiteComponent} from './content/sites/add-update-site/add-update-site.component';
import {PagesComponent} from './content/pages/pages.component';
import {AddUpdatePageComponent} from './content/pages/add-update-page/add-update-page.component';

const routes: Routes = [
  // Main layout routes
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuardService],
        data: { breadCrumbs: BreadCrumbConfig.getBreadCrumbConfig(RouteType.DASHBOARD)}
      },
      {
        path: 'sites',
        component: SitesComponent,
        canActivate: [AuthGuardService],
        data: { breadCrumbs: BreadCrumbConfig.getBreadCrumbConfig(RouteType.SITES), addUrl: '/sites/add', addText: 'BREADCRUMBS.sites.add'}
      },
      {
        path: 'sites/add',
        component: AddUpdateSiteComponent,
        canActivate: [AuthGuardService],
        data: {breadCrumbs: BreadCrumbConfig.getBreadCrumbConfig(RouteType.SITES_ADD)}
      },
      {
        path: 'sites/update/:id',
        component: AddUpdateSiteComponent,
        canActivate: [AuthGuardService],
        data: {breadCrumbs: BreadCrumbConfig.getBreadCrumbConfig(RouteType.SITES_UPDATE)}
      },
      {
        path: 'pages',
        component: PagesComponent,
        canActivate: [AuthGuardService],
        data: {breadCrumbs: BreadCrumbConfig.getBreadCrumbConfig(RouteType.PAGES), addUrl: '/pages/add', addText: 'BREADCRUMBS.pages.add'}
      },
      {
        path: 'pages/add',
        component: AddUpdatePageComponent,
        canActivate: [AuthGuardService],
        data: {breadCrumbs: BreadCrumbConfig.getBreadCrumbConfig(RouteType.PAGES_ADD)}
      },
      {
        path: 'sites/:siteId/pages/:id',
        component: AddUpdatePageComponent,
        canActivate: [AuthGuardService],
        data: {breadCrumbs: BreadCrumbConfig.getBreadCrumbConfig(RouteType.PAGES_UPDATE)}
      }
    ]
  },

  // Empty layout
  {
    path: '',
    component: EmptyLayoutComponent,
    children: [
      {path: 'login', component: LoginComponent},
      {path: '404', component: NotFoundComponent},
      {path: '**', component: NotFoundComponent},
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
