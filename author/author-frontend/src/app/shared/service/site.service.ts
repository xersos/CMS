import {Injectable} from '@angular/core';
import {RestService} from './rest.service';
import {AuthenticationService} from './authentication.service';
import {Observable, of} from 'rxjs';
import {PaginatedCollection} from '../models/paginated.collection';
import {catchError, map} from 'rxjs/operators';
import {Site} from '../models/entity/site';
import {MappingUtil} from '../utils/mapping.util';
import {catchSomethingWrong} from '../utils/functions';
import {EmitterService} from './emitterService';
import {HttpErrorResponse} from '@angular/common/http';

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

  public getSiteById(id: string): Observable<Site> {
    return this.restService.get(`/sites/${id}`, this.authService.getToken())
      .pipe(
        map((res: any) => MappingUtil.mapItemToSite(res)),
        catchSomethingWrong('siteError', null)
      );
  }

  public createSite(data: any): Observable<Site> {
    return this.restService.post(`/sites`, data, this.authService.getToken())
      .pipe(
        map((res: any) => MappingUtil.mapItemToSite(res)),
        catchError((error: HttpErrorResponse) => {
          if (error.error instanceof ErrorEvent) {
            EmitterService.of('siteError').emit('COMMON.ERRORS.something.wrong');
          } else {
            if (error.error.key === 'site_name_not_unique') {
              EmitterService.of('siteError').emit('SERVICE.ERRORS.SITE.name.not-unique');
            } else if (error.error.key === 'site_path_not_unique') {
              EmitterService.of('siteError').emit('SERVICE.ERRORS.SITE.path.not-unique');
            } else {
              EmitterService.of('siteError').emit('COMMON.ERRORS.something.wrong');
            }
          }

          return of(null);
        })
      );
  }

  public updateSite(id: string, data: any): Observable<Site> {
    return this.restService.put(`/sites/${id}`, data, this.authService.getToken())
      .pipe(
        map((res: any) => MappingUtil.mapItemToSite(res)),
        catchError((error: HttpErrorResponse) => {
          if (error.error instanceof ErrorEvent) {
            EmitterService.of('siteError').emit('COMMON.ERRORS.something.wrong');
          } else {
            if (error.error.key === 'site_name_not_unique') {
              EmitterService.of('siteError').emit('SERVICE.ERRORS.SITE.name.not-unique');
            } else if (error.error.key === 'site_path_not_unique') {
              EmitterService.of('siteError').emit('SERVICE.ERRORS.SITE.path.not-unique');
            } else {
              EmitterService.of('siteError').emit('COMMON.ERRORS.something.wrong');
            }
          }

          return of(null);
        })
      );
  }
}
