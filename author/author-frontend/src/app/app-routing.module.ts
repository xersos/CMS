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
