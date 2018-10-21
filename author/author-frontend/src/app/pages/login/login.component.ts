import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../shared/service/authentication.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {BaseComponent} from '../../base.component';
import {debounceTime, takeUntil} from 'rxjs/operators';
import {EmitterService} from '../../shared/service/emitterService';

@Component({
  selector: 'zcms-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent extends BaseComponent implements OnInit {

  public formErrors = {
    username: '',
    password: ''
  };

  public validationMessages = {
    username: {
      required: 'FORM.ERROR.required'
    },
    password: {
      required: 'FORM.ERROR.required'
    }
  };

  public form: FormGroup;
  public error: string = null;

  private redirectUrl: string = '/';

  constructor(private fb: FormBuilder, private authService: AuthenticationService, private router: Router,
              private route: ActivatedRoute) {
    super();

    this.route.queryParams
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((params: Params) => {
        if (params['redirectUrl'] && params['redirectUrl'] !== '') {
          this.redirectUrl = atob(params['redirectUrl']);
        }
        this.doneLoading();
      });

    EmitterService.of('loginError')
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((error: string) => {
        this.error = error;
      });
  }

  ngOnInit() {
    this.buildForm();
  }

  public submit(): void {
    if (this.form.dirty && this.form.valid) {
      this.loading();
      this.error = null;
      this.authService.authenticate(this.form.value.username, this.form.value.password)
        .pipe(takeUntil(this.ngUnsubscribe))
        .subscribe((res: boolean) => {
          if (res) {
            this.router.navigateByUrl(this.redirectUrl);
          }
        });
      this.doneLoading();
    }
  }

  private buildForm(): void {
    this.form = this.fb.group({
      username: ['', [<any>Validators.required]],
      password: ['', [<any>Validators.required]]
    });

    this.form.valueChanges
      .pipe(takeUntil(this.ngUnsubscribe), debounceTime(250))
      .subscribe(d => this.formErrors = this.onFormValueChanged(this.form, this.formErrors, this.validationMessages));

    this.formErrors = this.onFormValueChanged(this.form, this.formErrors, this.validationMessages);
  }

}
