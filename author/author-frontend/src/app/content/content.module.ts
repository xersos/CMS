import {NgModule} from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import {DashboardComponent} from './dashboard/dashboard.component';
import {SitesComponent} from './sites/sites.component';
import {AddUpdateSiteComponent} from './sites/add-update-site/add-update-site.component';
import {PagesComponent} from './pages/pages.component';
import {PagesTreeComponent} from './pages/pages-tree/pages-tree.component';
import {PagesTreeBranchComponent} from './pages/pages-tree/pages-tree-branch/pages-tree-branch.component';
import {AddUpdatePageComponent} from './pages/add-update-page/add-update-page.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    DashboardComponent,
    SitesComponent,
    AddUpdateSiteComponent,
    PagesComponent,
    PagesTreeComponent,
    PagesTreeBranchComponent,
    AddUpdatePageComponent
  ]
})
export class ContentModule {
}
