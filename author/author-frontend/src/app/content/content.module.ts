import { NgModule } from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SitesComponent } from './sites/sites.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [DashboardComponent, SitesComponent]
})
export class ContentModule { }
