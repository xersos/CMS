export class BreadCrumbItem {
  public name: string = null;
  public clickable: boolean = false;
  public uri: string = null;

  constructor(name: string, clickable?: boolean, uri?: string) {
    this.name = name;
    this.clickable = clickable || false;
    this.uri = uri || null;
  }
}
