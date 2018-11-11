import {Component, OnInit} from '@angular/core';
import {BaseComponent} from '../../../base.component';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Site} from '../../../shared/models/entity/site';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {SiteService} from '../../../shared/service/site.service';
import {FlashMessageService} from '../../../shared/service/flash-message.service';
import {EmitterService} from '../../../shared/service/emitterService';
import {debounceTime, finalize, mergeMap, takeUntil} from 'rxjs/operators';
import {EMPTY, Observable, of} from 'rxjs';
import {FlashMessage, FlashMessageType} from '../../../shared/models/flash.message';

@Component({
  selector: 'zcms-add-update-site',
  templateUrl: './add-update-site.component.html',
  styleUrls: ['./add-update-site.component.scss']
})
export class AddUpdateSiteComponent extends BaseComponent implements OnInit {

  public formErrors = {
    name: '',
    path: ''
  };

  public validationMessages = {
    name: {
      required: 'FORM.ERROR.required',
      minlength: 'FORM.ERROR.minlenght',
      maxlength: 'FORM.ERROR.maxlength'
    },
    path: {
      required: 'FORM.ERROR.required',
      pattern: 'FORM.ERROR.pattern',
    }
  };

  public validationMessagesValues = {
    name: {
      minlength: 1,
      maxlength: 255
    },
    path: {
      pattern: 'A-Z, a-z, 0-9, -, _, /'
    }
  };

  public form: FormGroup;
  public site: Site = null;
  public error: string = null;

  constructor(private fb: FormBuilder, private route: ActivatedRoute, private router: Router,
              private siteService: SiteService, private flashMessageService: FlashMessageService) {
    super();

    EmitterService.of('siteError')
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(error => this.error = error);
  }

  ngOnInit() {
    this.route.params.pipe(
      takeUntil(this.ngUnsubscribe),
      mergeMap((params: Params) => {
        if (params['id'] && params['id'] !== '') {
          this.loading();
          return this.siteService.getSiteById(params['id']);
        }

        return of(null);
      }),
    ).subscribe((site: Site) => {
      this.site = site;
      this.doneLoading();
      this.buildForm();
    });
  }

  public submit(): void {
    if (this.form.dirty && this.form.valid) {
      this.error = null;
      const toExec: Observable<Site> = this.site === null ?
        this.siteService.createSite(this.form.value) :
        this.siteService.updateSite(this.site.id, this.form.value);

      this.loading();
      toExec.pipe(
        takeUntil(this.ngUnsubscribe),
      ).subscribe((site: Site) => {
        if (site !== null) {
          this.flashMessageService.setMessage(new FlashMessage(FlashMessageType.SUCCESS,
            this.site === null ? 'COMPONENT.SITES.ADD.success' : 'COMPONENT.SITES.UPDATE.success'
          ));
          this.router.navigateByUrl('/sites');
        }
        this.doneLoading();
      });
    }
  }

  private buildForm(): void {
    this.form = this.fb.group({
      name: ['', [<any>Validators.required,
        <any>Validators.minLength(this.validationMessagesValues.name.minlength),
        <any>Validators.maxLength(this.validationMessagesValues.name.maxlength)]],
      path: ['', [<any>Validators.required, <any>Validators.pattern('^[a-zA-Z0-9-_/]*$')]]
    });

    this.fillForm();
    this.form.valueChanges.pipe(debounceTime(250), takeUntil(this.ngUnsubscribe))
      .subscribe(data => this.formErrors = this.onFormValueChanged(this.form, this.formErrors, this.validationMessages));
    this.formErrors = this.onFormValueChanged(this.form, this.formErrors, this.validationMessages);
  }

  private fillForm(): void {
    if (this.site !== null) {
      this.form.get('name').setValue(this.site.name);
      this.form.get('path').setValue(this.site.path);
    }
  }

}
