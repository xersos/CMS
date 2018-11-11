import {Component, OnInit} from '@angular/core';
import {BaseComponent} from '../../base.component';
import {PaginatedCollection} from '../../shared/models/paginated.collection';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {SiteService} from '../../shared/service/site.service';
import {finalize, takeUntil} from 'rxjs/operators';
import {EmitterService} from '../../shared/service/emitterService';

@Component({
  selector: 'zcms-sites',
  templateUrl: './sites.component.html',
  styleUrls: ['./sites.component.scss']
})
export class SitesComponent extends BaseComponent implements OnInit {

  public collection: PaginatedCollection = null;
  public currentPage: number = 1;

  public error: string = null;
  public success: string = null;

  constructor(private siteService: SiteService, private route: ActivatedRoute, private router: Router) {
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

}
