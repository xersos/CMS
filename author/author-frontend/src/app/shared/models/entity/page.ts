import {Site} from './site';

export class Page {
  public id: string;
  public name: string;
  public title: string;
  public path: string;
  public published: boolean;
  public site: Site;
  public parent: Page;
  public createdAt: Date;
  public updatedAt: Date;
}
