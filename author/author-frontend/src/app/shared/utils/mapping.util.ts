import {Site} from '../models/entity/site';
import {PaginatedCollection} from '../models/paginated.collection';
import {Page} from '../models/entity/page';

export class MappingUtil {

  public static mapReponseToPaginatedCollection(pageInfo: any, items: Array<any>): PaginatedCollection {
    const collection = new PaginatedCollection();

    collection.items = items;
    collection.totalItems = pageInfo.total;
    collection.totalPages = pageInfo.lastPage;
    collection.currentPage = pageInfo.currentPage;
    collection.first = pageInfo.first;
    collection.last = pageInfo.last;

    if (collection.totalPages > 5) {
      if (collection.currentPage - 2 < 1) {
        collection.pages.push(1, 2, 3, 4, 5);
      } else if (collection.currentPage + 2 > collection.totalPages) {
        for (let i = collection.totalPages - 4; i <= collection.totalPages; i++) {
          collection.pages.push(i);
        }
      } else {
        for (let i = collection.currentPage - 2; i <= collection.currentPage + 2; i++) {
          collection.pages.push(i);
        }
      }
    } else {
      for (let i = 1; i <= collection.totalPages; i++) {
        collection.pages.push(i);
      }
    }

    return collection;
  }

  public static mapItemToSite(item: any): Site {
    const site = new Site();

    site.id = item.id;
    site.name = item.name;
    site.path = item.path;
    site.createdAt = new Date(item.createdAt);
    site.updatedAt = new Date(item.updatedAt);

    return site;
  }

  public static mapItemToPage(item: any): Page {
    const page = new Page();

    page.id = item.id;
    page.name = item.name;
    page.title = item.title;
    page.description = item.description;
    page.published = item.published;
    page.createdAt = new Date(item.createdAt);
    page.updatedAt = new Date(item.updatedAt);

    return page;
  }
}
