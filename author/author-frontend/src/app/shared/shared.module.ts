import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {HttpLoaderFactory} from './utils/functions';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {JwtModule} from '@auth0/angular-jwt';
import {AlertModule} from 'ngx-bootstrap';
import { PaginationComponent } from './comonent/pagination/pagination.component';
import { FlashMessageComponent } from './comonent/flash-message/flash-message.component';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,

    JwtModule.forRoot({
      config: {
        tokenGetter: () => 'no-token'
      }
    }),

    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),

    // Common bootstrap modules
    AlertModule.forRoot(),

  ],
  declarations: [PaginationComponent, FlashMessageComponent],
  exports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    TranslateModule,

    AlertModule,

    PaginationComponent
  ]
})
export class SharedModule { }
