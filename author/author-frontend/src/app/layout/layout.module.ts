import { NgModule } from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import { EmptyLayoutComponent } from './empty-layout/empty-layout.component';
import { MainLayoutComponent } from './main-layout/main-layout.component';
import {StructureModule} from '../structure/structure.module';

@NgModule({
  imports: [
    SharedModule,
    StructureModule
  ],
  declarations: [
    EmptyLayoutComponent,
    MainLayoutComponent
  ]
})
export class LayoutModule { }
