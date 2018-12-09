import {BreadCrumbItem} from '../models/bread.crumb.item';
import {RouteType} from '../enums/route.type';

export class BreadCrumbConfig {

  public static getBreadCrumbConfig(routeType: RouteType): BreadCrumbItem[] {
    switch (routeType) {
      case RouteType.DASHBOARD:
        return [item('BREADCRUMBS.dashboard')];
      case RouteType.SITES:
        return [item('BREADCRUMBS.sites')];
      case RouteType.SITES_ADD:
        return [item('BREADCRUMBS.sites', true, '/sites'), item('BREADCRUMBS.sites.add')];
      case RouteType.SITES_UPDATE:
        return [item('BREADCRUMBS.sites', true, '/sites'), item('BREADCRUMBS.sites.update')];
      case RouteType.PAGES:
        return [item('BREADCRUMBS.pages')];
      case RouteType.PAGES_ADD:
        return [item('BREADCRUMBS.pages', true, '/pages'), item('BREADCRUMBS.pages.add')];
      case RouteType.PAGES_UPDATE:
        return [item('BREADCRUMBS.pages', true, '/pages'), item('BREADCRUMBS.pages.update')];
      case RouteType.PLUGINS:
        return [item('BREADCRUMBS.plugins')];
      case RouteType.PLUGINS_ADD:
        return [item('BREADCRUMBS.plugins', true, '/plugins'), item('BREADCRUMBS.plugins.add')];
    }

    return [];
  }
}

function item(labelKey: string, clickable?: boolean, uri?: string): BreadCrumbItem {
  return new BreadCrumbItem(labelKey, clickable, uri);
}
