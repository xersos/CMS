import {Component, OnInit} from '@angular/core';
import {Site} from '../../shared/models/entity/site';
import {TreeViewItem} from '../../shared/models/tree.view.item';
import {BaseComponent} from '../../base.component';
import {PageService} from '../../shared/service/page.service';
import {takeUntil} from 'rxjs/operators';
import {PaginatedCollection} from '../../shared/models/paginated.collection';
import {Page} from '../../shared/models/entity/page';

@Component({
  selector: 'zcms-pages',
  templateUrl: './pages.component.html',
  styleUrls: ['./pages.component.scss']
})
export class PagesComponent extends BaseComponent implements OnInit {

  public collection: PaginatedCollection = null;
  public currentPage: number = 1;

  public error: string = null;
  public success: string = null;

  public site: Site = null;

  private treeItem: TreeViewItem = null;

  constructor(private pageService: PageService) {
    super();
  }

  ngOnInit() {
  }

  public onSiteSelected(site: Site): void {
    this.site = site;
  }

  public onTreeItemSelected(item: TreeViewItem): void {
    this.treeItem = item;
    this.setPage(1);
  }

  public setPage(page: number): void {
    if (this.site !== null && this.treeItem !== null) {
      this.loading();
      this.pageService.getPagesBySiteAndParent(this.site.id, page, this.treeItem.id, !!this.treeItem.isSite)
        .pipe(takeUntil(this.ngUnsubscribe))
        .subscribe((collection: PaginatedCollection) => {
          if (collection != null) {
            this.collection = collection;
            this.currentPage = page;
          }
          this.doneLoading();
        });
    }
  }

  public askForPageDeletion(page: Page): void {

  }

}
