import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RestService {

  private readonly baseUrl: string = environment.baseApiUrl;

  constructor(private http: HttpClient) { }

  public getUserToken(username: string, password: string): Observable<any> {
    const body = new HttpParams()
      .set('grant_type', 'password')
      .set('username', username)
      .set('password', password);
    const headers = RestService.headersForOAuth(true);

    return this.http.post(this.baseUrl + '/oauth/token', body.toString(), {headers});
  }

  public getPublicToken(username: string, password: string): Observable<any> {
    const body = new HttpParams()
      .set('grant_type', 'client_credentials');
    const headers = RestService.headersForOAuth(false);

    return this.http.post(this.baseUrl + '/oauth/token', body.toString(), {headers});
  }

  public refreshToken(refreshToken: string): Observable<any> {
    const body = new HttpParams()
      .set('grant_type', 'refresh_token')
      .set('refresh_token', refreshToken);
    const headers = RestService.headersForOAuth(true);

    return this.http.post(this.baseUrl + '/oauth/token', body.toString(), {headers});
  }

  public get(endpoint: string, token: string): Observable<any> {
    const headers = RestService.setAuthorizationHeader(token)
      .set('Content-Type', 'application/json');

    return this.http.get(this.baseUrl + endpoint, {headers});
  }

  public post(endpoint: string, data: any, token: string): Observable<any> {
    const headers = RestService.setAuthorizationHeader(token)
      .set('Content-Type', 'application/json');

    return this.http.post(this.baseUrl + endpoint, data, {headers});
  }

  public put(endpoint: string, data: any, token: string): Observable<any> {
    const headers = RestService.setAuthorizationHeader(token)
      .set('Content-Type', 'application/json');

    return this.http.put(this.baseUrl + endpoint, data, {headers});
  }

  public delete(endpoint: string, token: string): Observable<any> {
    const headers = RestService.setAuthorizationHeader(token)
      .set('Content-Type', 'application/json');

    return this.http.delete(this.baseUrl + endpoint, {headers});
  }

  public upload(endpoint: string, data: FormData, token: string): Observable<any> {
    const headers = RestService.setAuthorizationHeader(token);

    return this.http.post(this.baseUrl + endpoint, data, {headers});
  }

  private static headersForOAuth(isApp: boolean): HttpHeaders {
    const authString = btoa( (isApp ? 'app' : 'public') + ':' + environment.oauthSecret);

    return new HttpHeaders()
      .set('Authorization', 'Basic ' + authString)
      .set('Content-Type', 'application/x-www-form-urlencoded');
  }

  private static setAuthorizationHeader(token: string): HttpHeaders {
    return new HttpHeaders().set('Authorizaion', 'Bearer ' + token);
  }
}
