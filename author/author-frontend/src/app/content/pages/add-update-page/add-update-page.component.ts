import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {BaseComponent} from '../../../base.component';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {PageService} from '../../../shared/service/page.service';
import {FlashMessageService} from '../../../shared/service/flash-message.service';
import {debounceTime, map, mergeMap, takeUntil} from 'rxjs/operators';
import {Observable, of} from 'rxjs';
import {Page} from '../../../shared/models/entity/page';
import {Site} from '../../../shared/models/entity/site';
import {SiteService} from '../../../shared/service/site.service';
import {PaginatedCollection} from '../../../shared/models/paginated.collection';
import {FlashMessage, FlashMessageType} from '../../../shared/models/flash.message';
import {EmitterService} from '../../../shared/service/emitterService';

@Component({
  selector: 'zcms-add-update-page',
  templateUrl: './add-update-page.component.html',
  styleUrls: ['./add-update-page.component.scss']
})
export class AddUpdatePageComponent extends BaseComponent implements OnInit {

  public formErrors = {
    site: '',
    parent: '',
    title: '',
    name: ''
  };

  public validationMessages = {
    site: {
      required: 'FORM.ERROR.required',
    },
    parent: {
      required: 'FORM.ERROR.required',
    },
    title: {
      required: 'FORM.ERROR.required',
      minlength: 'FORM.ERROR.minlenght',
      maxlength: 'FORM.ERROR.maxlength'
    },
    name: {
      required: 'FORM.ERROR.required',
      minlength: 'FORM.ERROR.minlenght',
      maxlength: 'FORM.ERROR.maxlength',
      pattern: 'FORM.ERROR.pattern'
    }
  };

  public validationMessagesValues = {
    title: {
      minlength: 1,
      maxlength: 255,
    },
    name: {
      minlength: 1,
      maxlength: 255,
      pattern: 'A-Z, a-z, 0-9, -, _, ='
    },
  };

  public form: FormGroup;
  public error: string = null;

  public siteOptions: NgSelectObject[] = [];
  public pageOptions: NgSelectObject[] = [];

  public page: Page = null;
  private site: Site = null;

  private cachedPages: Map<string, NgSelectObject[]> = new Map<string, NgSelectObject[]>();

  constructor(private fb: FormBuilder, private route: ActivatedRoute, private router: Router,
              private pageService: PageService, private siteService: SiteService,
              private flashMessageService: FlashMessageService) {
    super();

    EmitterService.of('pageError')
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((error: string) => {
        this.error = error;
        this.doneLoading();
      });
  }

  ngOnInit() {
    const updatePageOb = this.route.params.pipe(
      takeUntil(this.ngUnsubscribe),
      mergeMap((params: Params) => {
        if (params['siteId'] && params['siteId'] !== '' && params['id'] && params['id'] !== '') {
          this.loading();
          return this.pageService.getPageById(params['siteId'], params['id']);
        }

        return of(null);
      })
    );

    this.siteService.getSites(1, -1).pipe(
      takeUntil(this.ngUnsubscribe),
      map(collection => {
        const options = [];
        for (const option of collection.items) {
          options.push({
            id: option.id,
            text: option.name
          });
        }

        return options;
      }),
      mergeMap((sites: NgSelectObject[]) => {
        this.siteOptions = sites;
        return updatePageOb;
      })
    ).subscribe((page: Page) => {
      this.page = page;
      this.buildForm();
      this.doneLoading();
    });
  }

  public submit(): void {
    if (this.form.dirty && this.form.valid) {
      this.error = null;
      const toExec: Observable<boolean> = this.page === null ?
        this.pageService.createSite(this.form.value) :
        this.pageService.updateSite(this.site.id, this.page.id, this.form.value);

      this.loading();
      toExec.pipe(
        takeUntil(this.ngUnsubscribe)
      ).subscribe((res: boolean) => {
        if (res) {
          this.flashMessageService.setMessage(new FlashMessage(FlashMessageType.SUCCESS,
            this.page === null ? 'COMPONENT.PAGES.ADD.success' : 'COMPONENT.PAGES.UPDATE.success'));
          this.router.navigateByUrl('/pages');
        }
        this.doneLoading();
      });
    }
  }

  public onSiteSelected(id: string): void {
    if (this.cachedPages.has(id)) {
      this.pageOptions = this.cachedPages.get(id);
      this.form.get('parent').enable();
    } else {
      this.loading();
      this.pageService.getPagesBySite(id, 1, -1).pipe(
        takeUntil(this.ngUnsubscribe),
        map((collection: PaginatedCollection) => {
          const options: NgSelectObject[] = [];
          for (const item of collection.items) {
            options.push({
              id: item.id,
              text: item.path,
              name: item.name
            });
          }

          options.sort((a, b) => {
            if (a.text < b.text) {
              return -1;
            }
            if (a.text > b.text) {
              return 1;
            }
            return 0;
          });

          return options;
        })
      ).subscribe((options: NgSelectObject[]) => {
        this.cachedPages.set(id, options);
        this.pageOptions = this.cachedPages.get(id);
        this.doneLoading();
        this.form.get('parent').enable();

        if (this.page && this.page.parent) {
          this.form.get('parent').setValue(this.page.parent.id);
        }
      });
    }
  }

  public onSiteRemoved(): void {
    this.form.get('parent').disable();
    this.form.get('parent').setValue('');
  }

  private buildForm(): void {
    this.form = this.fb.group({
      site: new FormControl('', [
        <any>Validators.required,
      ]),
      parent: new FormControl('', [
        <any>Validators.required,
      ]),
      title: new FormControl('', [
        <any>Validators.required,
        <any>Validators.minLength(this.validationMessagesValues.title.minlength),
        <any>Validators.maxLength(this.validationMessagesValues.title.maxlength),
      ]),
      name: new FormControl('', [
        <any>Validators.required,
        <any>Validators.minLength(this.validationMessagesValues.name.minlength),
        <any>Validators.maxLength(this.validationMessagesValues.name.maxlength),
        <any>Validators.pattern('^[a-zA-Z0-9-_=]*$')
      ])
    });

    this.form.get('parent').disable();
    this.fillForm();
    this.form.valueChanges.pipe(debounceTime(250), takeUntil(this.ngUnsubscribe))
      .subscribe(() => this.formErrors = this.onFormValueChanged(this.form, this.formErrors, this.validationMessages));
    this.formErrors = this.onFormValueChanged(this.form, this.formErrors, this.validationMessages);
  }

  private fillForm(): void {
    if (this.page != null) {
      this.site = this.page.site;
      this.form.get('title').setValue(this.page.title);
      this.form.get('name').setValue(this.page.name);
      // todo selection stuffies

      this.form.get('site').disable();
      this.form.get('site').setValue(this.site.id);
      this.onSiteSelected(this.site.id);
    }
  }
}

class NgSelectObject {
  id: string;
  text: string;
  name: string;
}
