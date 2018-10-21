import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {HttpLoaderFactory} from './utils/functions';
import {HttpClient, HttpClientModule} from '@angular/common/http';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,

    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),

  ],
  declarations: [],
  exports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    TranslateModule
  ]
})
export class SharedModule { }
