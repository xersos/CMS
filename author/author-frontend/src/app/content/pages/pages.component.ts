import { Component, OnInit } from '@angular/core';
import {Site} from '../../shared/models/entity/site';
import {TreeViewItem} from '../../shared/models/tree.view.item';
import {BaseComponent} from '../../base.component';

@Component({
  selector: 'zcms-pages',
  templateUrl: './pages.component.html',
  styleUrls: ['./pages.component.scss']
})
export class PagesComponent extends BaseComponent implements OnInit {

  private currentPage: number = 1;
  private treeItem: TreeViewItem = null;
  private site: Site = null;

  constructor() {
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
    // if (this.site !== null && this.treeItem !== null) {
    //   this.loading();
    //   this.siteService.getPagesBySiteAndParent(this.site.id, this.treeItem.id, true)
    //     .pipe(takeUntil(this.ngUnsubscribe))
    //     .subscribe((collection: PaginatedCollection) => {
    //       if (collection != null) {
    //         this.collection = collection;
    //         this.currentPage = page;
    //       }
    //       this.doneLoading();
    //     });
    // }
  }

}
