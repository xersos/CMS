import {NgModule} from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import { NotFoundComponent } from './not-found/not-found.component';
import { LoginComponent } from './login/login.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [NotFoundComponent, LoginComponent]
})
export class PagesModule { }
