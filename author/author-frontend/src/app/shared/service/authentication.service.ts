import {Injectable} from '@angular/core';
import {RestService} from './rest.service';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Observable, of} from 'rxjs';
import {catchError, delay, map} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {EmitterService} from './emitterService';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private restService: RestService, private jwtHelper: JwtHelperService) {
  }

  public authenticate(username: string, password: string): Observable<boolean> {
    return this.restService.getUserToken(username, password).pipe(
      map((data: any) => {
        const tokenInfo = {
          accessToken: data.access_token,
          refreshToken: data.refresh_token
        };

        sessionStorage.setItem('token-info', JSON.stringify(tokenInfo));

        this.refreshAfterDelay();
        return true;
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.error instanceof ErrorEvent) {
          EmitterService.of('loginError').emit('COMMON.ERRORS.something.wrong');
        } else {
          EmitterService.of('loginError').emit('SERVICE.ERRORS.AUTH.invalid.credentials');
        }

        return of(false);
      })
    );
  }

  public getToken(): string {
    const tokenInfo = sessionStorage.getItem('token-info');

    if (!tokenInfo || !JSON.parse(tokenInfo).accessToken) {
      return null;
    }

    const accessToken = JSON.parse(tokenInfo).accessToken;

    if (!this.isValid(accessToken)) {
      return null;
    }

    return accessToken;
  }

  public getTokenInfo(): any {
    const tokenInfo = sessionStorage.getItem('token-info');

    if (!tokenInfo) {
      return null;
    }

    return JSON.parse(tokenInfo);
  }

  public refreshToken(): Observable<boolean> {
    const tokenInfo = this.getTokenInfo();

    if (tokenInfo === null) {
      of(false);
    }

    return this.restService.refreshToken(tokenInfo.refreshToken).pipe(
      map((data: any) => {
        const newTokenInfo = {
          accessToken: data.access_token,
          refreshToken: data.refresh_token
        };

        sessionStorage.setItem('token-info', JSON.stringify(newTokenInfo));

        return true;
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.error instanceof ErrorEvent) {
          EmitterService.of('loginError').emit('COMMON.ERRORS.something.wrong');
        } else {
          EmitterService.of('loginError').emit('SERVICE.ERRORS.AUTH.invalid.credentials');
        }

        return of(false);
      })
    );
  }

  public logout(): void {
    sessionStorage.removeItem('token-info');
  }

  public isAuthorized(): boolean {
    return this.getToken() !== null;
  }

  private refreshAfterDelay(): void {
    const token: string = this.getToken();

    if (token !== null) {
      of('').pipe(delay(3000 * 1000))
        .subscribe(res => {
          this.refreshToken().subscribe((result: boolean) => {
            if (result) {
              this.refreshAfterDelay();
            } else {
              this.logout();
            }
          });
        });
    }
  }

  private isValid(token: string): boolean {
    return !this.jwtHelper.isTokenExpired(token);
  }
}
