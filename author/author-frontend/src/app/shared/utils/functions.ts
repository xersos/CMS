import {HttpClient} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {of, OperatorFunction} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {EmitterService} from '../service/emitterService';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n', '.json');
}

export function catchSomethingWrong<T, R>(errorID: string, returnValue: any): OperatorFunction<T, R> {
  return catchError(err => {
    EmitterService.of(errorID).emit('COMMON.ERRORS.something.wrong');
    return of(returnValue);
  });
}

export function Replace(el: any): any {
  const nativeElement: HTMLElement = el.nativeElement;
  const parentElement: HTMLElement = nativeElement.parentElement;
  // move all children out of the element
  while (nativeElement.firstChild) {
    parentElement.insertBefore(nativeElement.firstChild, nativeElement);
  }
  // remove the empty element(the host)
  parentElement.removeChild(nativeElement);
}
