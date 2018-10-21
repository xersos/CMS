import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {SharedModule} from './shared/shared.module';
import {PagesModule} from './pages/pages.module';
import {ContentModule} from './content/content.module';
import {LayoutModule} from './layout/layout.module';
import {AppRoutingModule} from './app-routing.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    SharedModule,

    LayoutModule,
    PagesModule,
    ContentModule,

    AppRoutingModule,
    BrowserModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
