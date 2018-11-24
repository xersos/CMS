import {Injectable} from '@angular/core';
import {PaginatedCollection} from '../models/paginated.collection';
import {Observable, of, OperatorFunction} from 'rxjs';
import {RestService} from './rest.service';
import {AuthenticationService} from './authentication.service';
import {catchError, map} from 'rxjs/operators';
import {MappingUtil} from '../utils/mapping.util';
import {catchSomethingWrong} from '../utils/functions';
import {Page} from '../models/entity/page';
import {Site} from '../models/entity/site';
import {HttpErrorResponse} from '@angular/common/http';
import {EmitterService} from './emitterService';

@Injectable({
  providedIn: 'root'
})
export class PageService {

  constructor(private restService: RestService, private authService: AuthenticationService) {
  }

  public getPagesBySiteAndParent(siteId: string, page: number, parentPageId: string, includeParent: boolean)
    : Observable<PaginatedCollection> {
    return this.restService.get(`/sites/${siteId}/pages?page=${page}&parent=${parentPageId}&includeParent=${includeParent}`,
      this.authService.getToken())
      .pipe(
        this.mapPageCollection(),
        catchSomethingWrong('pageError', null)
      );
  }

  public getPagesBySite(siteId: string, page: number, pageSize: number = 50): Observable<PaginatedCollection> {
    if (pageSize <= 0) {
      page = 0;
      pageSize = Number.MAX_SAFE_INTEGER;
    }

    return this.restService.get(`/sites/${siteId}/pages?page=${page}&size=${pageSize}`, this.authService.getToken())
      .pipe(
        this.mapPageCollection(),
        catchSomethingWrong('pageError', null)
      );
  }

  public getPageById(siteId: string, id: string): Observable<Page> {
    return this.restService.get(`/sites/`, this.authService.getToken());
  }

  public createSite(formData: any): Observable<boolean> {
    const siteId: string = formData.site;
    const body = {
      title: formData.title,
      name: formData.name,
      parent: formData.parent
    };

    return this.restService.post(`/sites/${siteId}/pages`, body, this.authService.getToken()).pipe(
      map(res => true),
      catchError((error: HttpErrorResponse) => {
        if (error.error instanceof ErrorEvent) {
          EmitterService.of('pageError').emit('COMMON.ERRORS.something.wrong');
        } else {
          if (error.error.key === 'page_name_not_unique') {
            EmitterService.of('pageError').emit('SERVICE.ERRORS.PAGE.name.not-unique');
          } else {
            EmitterService.of('siteError').emit('COMMON.ERRORS.something.wrong');
          }
        }

        return of(null);
      })
    );
  }

  public updateSite(pageId: string, formData: any): Observable<boolean> {
    return of(true);
  }

  private mapPageCollection(): OperatorFunction<any, PaginatedCollection> {
    return map(res => {
      const pages: Page[] = [];
      for (const item of res.items) {
        pages.push(MappingUtil.mapItemToPage(item));
      }

      return MappingUtil.mapReponseToPaginatedCollection(res, pages);
    });
  }
}
