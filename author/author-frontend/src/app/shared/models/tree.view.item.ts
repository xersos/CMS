export class TreeViewItem {
  public id: string;
  public name: string;
  public children: TreeViewItem[] = [];
  public isOpen: boolean = false;
}
