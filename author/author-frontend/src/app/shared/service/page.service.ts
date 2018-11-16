import {Injectable} from '@angular/core';
import {PaginatedCollection} from '../models/paginated.collection';
import {Observable} from 'rxjs';
import {RestService} from './rest.service';
import {AuthenticationService} from './authentication.service';
import {map} from 'rxjs/operators';
import {MappingUtil} from '../utils/mapping.util';
import {catchSomethingWrong} from '../utils/functions';
import {Page} from '../models/entity/page';

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
        map(res => {
          const pages: Page[] = [];
          for (const item of res.items) {
            pages.push(MappingUtil.mapItemToPage(item));
          }

          return MappingUtil.mapReponseToPaginatedCollection(res, pages);
        }),

        catchSomethingWrong('pageError', null)
      );
  }
}
