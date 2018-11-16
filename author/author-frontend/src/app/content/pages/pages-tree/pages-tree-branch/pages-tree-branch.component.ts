import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TreeViewItem} from '../../../../shared/models/tree.view.item';

@Component({
  selector: '[zcms-pages-tree-branch]',
  templateUrl: './pages-tree-branch.component.html',
  styleUrls: ['./pages-tree-branch.component.scss']
})
export class PagesTreeBranchComponent implements OnInit {

  @Input() public branchItem: TreeViewItem;

  @Output() public onSelectedItem: EventEmitter<TreeViewItem> = new EventEmitter<TreeViewItem>();

  constructor() { }

  ngOnInit() {
    // convert undefined to boolean
    this.branchItem.isOpen = !!this.branchItem.isOpen;
  }

  public toggle(id: string): boolean {
    if (this.branchItem.isOpen) {
      this.closeChildren(this.branchItem.children);
    }
    this.branchItem.isOpen = !this.branchItem.isOpen;

    return false;
  }

  public select(item: TreeViewItem): boolean {
    this.onSelectedItem.emit(item);
    return false;
  }

  public onSelect(item: TreeViewItem): void {
    this.onSelectedItem.emit(item);
  }

  private closeChildren(children: TreeViewItem[]): void {
    for (const child of children) {
      child.isOpen = false;
      if (child.children.length > 0) {
        this.closeChildren(child.children);
      }
    }
  }

}
