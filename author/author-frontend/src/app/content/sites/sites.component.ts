import {Component, OnInit} from '@angular/core';
import {BaseComponent} from '../../base.component';
import {PaginatedCollection} from '../../shared/models/paginated.collection';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {SiteService} from '../../shared/service/site.service';
import {finalize, take, takeUntil} from 'rxjs/operators';
import {EmitterService} from '../../shared/service/emitterService';
import {TranslateService} from '@ngx-translate/core';
import {Site} from '../../shared/models/entity/site';
import {BsModalRef, BsModalService, ModalOptions} from 'ngx-bootstrap';
import {ConfirmModalComponent} from '../../shared/modal/confirm-modal/confirm-modal.component';

@Component({
  selector: 'zcms-sites',
  templateUrl: './sites.component.html',
  styleUrls: ['./sites.component.scss']
})
export class SitesComponent extends BaseComponent implements OnInit {

  private static readonly DELETE_CONFIRM_KEYS: string[] = [
    'COMPONENT.SITES.DELETE.title',
    'COMPONENT.SITES.DELETE.body',
    'COMPONENT.SITES.DELETE.ok',
    'COMPONENT.SITES.DELETE.close',
  ];

  public collection: PaginatedCollection = null;
  public currentPage: number = 1;

  public error: string = null;
  public success: string = null;

  constructor(private siteService: SiteService, private translate: TranslateService, private modalService: BsModalService,
              private route: ActivatedRoute, private router: Router) {
    super();
  }

  ngOnInit() {
    this.route.queryParams
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((params: Params) => {
        if (params['page'] && !Number.isInteger(+params['page'])) {
          this.setPage(+params['page']);
        } else {
          this.setPage(1);
        }
      });

    EmitterService.of('siteError')
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((error: string) => this.error = error);
  }

  public setPage(page: number): void {
    this.loading();
    this.error = null;
    this.success = null;
    this.siteService.getSites(page)
      .pipe(
        takeUntil(this.ngUnsubscribe),
      ).subscribe((collection: PaginatedCollection) => {
      if (collection != null) {
        this.currentPage = page;
        this.collection = collection;
        this.router.navigateByUrl(`/sites?page=${page}`);
      }
      this.doneLoading();
    });
  }

  public askForSiteDeletion(site: Site): void {
    const initialConfirmState = this.buildConfirmModalState(site.name);
    const modalOptions: ModalOptions = {
      initialState: initialConfirmState
    };
    const modalRef: BsModalRef = this.modalService.show(ConfirmModalComponent, modalOptions);

    modalRef.content.confirmed.pipe(take(1))
      .subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.deleteSite(site);
        }
      });
  }

  private buildConfirmModalState(siteName: string): any {
    const translations = this.translate.instant(SitesComponent.DELETE_CONFIRM_KEYS, {siteName: siteName});

    return {
      title: translations[SitesComponent.DELETE_CONFIRM_KEYS[0]],
      body: translations[SitesComponent.DELETE_CONFIRM_KEYS[1]],
      okBtn: translations[SitesComponent.DELETE_CONFIRM_KEYS[2]],
      closeBtn: translations[SitesComponent.DELETE_CONFIRM_KEYS[3]],
    };
  }

  private deleteSite(site: Site): void {
    this.loading();
    this.siteService.deleteSite(site.id).pipe(
      takeUntil(this.ngUnsubscribe)
    ).subscribe((res: boolean) => {
      if (res) {
        this.success = 'COMPONENT.SITES.DELETE.success';
        this.setPage(this.currentPage);
      }
      this.doneLoading();
    });
  }

}
