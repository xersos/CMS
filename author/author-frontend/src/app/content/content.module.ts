import { NgModule } from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SitesComponent } from './sites/sites.component';
import { AddUpdateSiteComponent } from './sites/add-update-site/add-update-site.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [DashboardComponent, SitesComponent, AddUpdateSiteComponent]
})
export class ContentModule { }
