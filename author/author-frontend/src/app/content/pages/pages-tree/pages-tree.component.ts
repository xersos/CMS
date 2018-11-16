import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {BaseComponent} from '../../../base.component';
import {Site} from '../../../shared/models/entity/site';
import {SiteService} from '../../../shared/service/site.service';
import {takeUntil} from 'rxjs/operators';
import {PaginatedCollection} from '../../../shared/models/paginated.collection';
import {TreeViewItem} from '../../../shared/models/tree.view.item';

@Component({
  selector: 'zcms-pages-tree',
  templateUrl: './pages-tree.component.html',
  styleUrls: ['./pages-tree.component.scss']
})
export class PagesTreeComponent extends BaseComponent implements OnInit {

  @Output() onTreeItemSelected: EventEmitter<TreeViewItem> = new EventEmitter<TreeViewItem>();
  @Output() onSiteSelected: EventEmitter<Site> = new EventEmitter<Site>();

  public sites: Site[] = [];
  public treeViews: Map<string, TreeViewItem> = new Map<string, TreeViewItem>();
  public openTreeview: string = null;

  constructor(private siteService: SiteService) {
    super();
  }

  ngOnInit() {
    this.loading();
    this.siteService.getSites(1, false).pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((collection: PaginatedCollection) => {
        if (collection != null) {
          this.sites = collection.items;
        }
        this.doneLoading();
      });
  }

  public toggleTreeView(site: Site): boolean {
    if (!this.treeViews.has(site.id)) {
      this.loading();
      this.siteService.getPageTree(site.id).pipe(takeUntil(this.ngUnsubscribe))
        .subscribe((treeView: TreeViewItem) => {
          if (treeView) {
            this.treeViews.set(site.id, treeView);
            this.openTreeview = site.id;
            this.siteSelected(site);
          }
          this.doneLoading();
        });
    } else if (site.id === this.openTreeview) {
      this.openTreeview = null;
    } else {
      this.openTreeview = site.id;
    }

    if (this.openTreeview) {
      this.siteSelected(site);
    }

    return false;
  }

  public getRootBranches(siteId: string): TreeViewItem[] {
    const treeView: TreeViewItem = this.treeViews.get(siteId);
    return treeView.children;
  }

  public onItemSelect(item: TreeViewItem): void {
    this.onTreeItemSelected.emit(item);
  }

  public siteSelected(site: Site): boolean {
    if (!this.treeViews.has(site.id)) {
      this.loading();
      this.siteService.getPageTree(site.id).pipe(takeUntil(this.ngUnsubscribe))
        .subscribe((treeView: TreeViewItem) => {
          if (treeView) {
            this.treeViews.set(site.id, treeView);
            this.onSiteSelected.emit(site);
            this.onItemSelect(this.treeViews.get(site.id));
          }
          this.doneLoading();
        });
    } else {
      this.onSiteSelected.emit(site);
      this.onItemSelect(this.treeViews.get(site.id));
    }

    return false;
  }

}
