import {Injectable} from '@angular/core';
import {RestService} from './rest.service';
import {AuthenticationService} from './authentication.service';
import {Observable} from 'rxjs';
import {PaginatedCollection} from '../models/paginated.collection';
import {map} from 'rxjs/operators';
import {Site} from '../models/entity/site';
import {MappingUtil} from '../utils/mapping.util';
import {catchSomethingWrong} from '../utils/functions';

@Injectable({
  providedIn: 'root'
})
export class SiteService {

  constructor(private restService: RestService, private authService: AuthenticationService) {
  }

  public getSites(page: number): Observable<PaginatedCollection> {
    return this.restService.get(`/sites?page=${page}`, this.authService.getToken()).pipe(
      map(res => {
        const sites: Site[] = [];
        for (const item of res.items) {
          sites.push(MappingUtil.mapItemToSite(item));
        }

        return MappingUtil.mapReponseToPaginatedCollection(res, sites);
      }),
      catchSomethingWrong('siteError', null)
    );
  }
}
