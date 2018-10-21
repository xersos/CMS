import { NgModule } from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import {
  AsideToggleDirective,
  BrandMinimizeDirective,
  MobileSidebarToggleDirective,
  SidebarMinimizeDirective,
  SidebarOffCanvasCloseDirective,
  SidebarToggleDirective
} from './structure.directives';
import {BsDropdownModule, TabsModule} from 'ngx-bootstrap';
import { BreadcrumbComponent } from './breadcrumb/breadcrumb.component';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { LeftSidebarComponent } from './left-sidebar/left-sidebar.component';
import { RightSidebarComponent } from './right-sidebar/right-sidebar.component';
import {AppSidebarModule} from '@coreui/angular';

@NgModule({
  imports: [
    SharedModule,

    BsDropdownModule.forRoot(),
    TabsModule.forRoot(),

    AppSidebarModule
  ],
  declarations: [
    SidebarToggleDirective,
    SidebarMinimizeDirective,
    MobileSidebarToggleDirective,
    SidebarOffCanvasCloseDirective,
    BrandMinimizeDirective,
    AsideToggleDirective,

    BreadcrumbComponent,
    FooterComponent,
    HeaderComponent,
    LeftSidebarComponent,
    RightSidebarComponent,
  ],
  exports: [
    BreadcrumbComponent,
    FooterComponent,
    HeaderComponent,
    LeftSidebarComponent,
    RightSidebarComponent
  ]
})
export class StructureModule { }
