import {Injectable} from '@angular/core';
import {RestService} from './rest.service';
import {AuthenticationService} from './authentication.service';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {MappingUtil} from '../utils/mapping.util';
import {catchSomethingWrong} from '../utils/functions';
import {Plugin} from '../models/entity/plugin';

@Injectable({
  providedIn: 'root'
})
export class PluginService {

  constructor(private restService: RestService, private authService: AuthenticationService) {

  }

  public getPlugins(): Observable<Plugin[]> {
    return this.restService.get('/plugins', this.authService.getToken())
      .pipe(
        map((items: any[]) => {
          const plugins: Plugin[] = [];
          for (const item of items) {
            plugins.push(MappingUtil.mapItemToPlugin(item));
          }

          return plugins;
        }),
        catchSomethingWrong('pluginError', [])
      );
  }

  public startPlugin(id: string): Observable<boolean> {
    return this.restService.post(`/plugins/${id}/start`, {}, this.authService.getToken())
      .pipe(
        map(res => true),
        catchSomethingWrong('pluginError', false)
      );
  }

  public stopPlugin(id: string): Observable<boolean> {
    return this.restService.post(`/plugins/${id}/stop`, {}, this.authService.getToken())
      .pipe(
        map(res => true),
        catchSomethingWrong('pluginError', false)
      );
  }
}
