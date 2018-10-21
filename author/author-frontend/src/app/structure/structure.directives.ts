import {Directive, HostListener, Input, OnInit} from '@angular/core';

@Directive({
  selector: '[zcmsSidebarToggler]'
})
export class SidebarToggleDirective implements OnInit {
  @Input('zcmsSidebarToggler') breakpoint: string;

  public bp;

  constructor() {
  }

  ngOnInit(): void {
    this.bp = this.breakpoint;
  }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    let cssClass;
    this.bp ? cssClass = `sidebar-${this.bp}-show` : cssClass = sidebarCssClasses[0];
    ToggleClasses(cssClass, sidebarCssClasses);
  }
}

@Directive({
  selector: '[zcmsSidebarMinimizer]'
})
export class SidebarMinimizeDirective {
  constructor() {
  }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    document.querySelector('body').classList.toggle('sidebar-minimized');
  }
}

@Directive({
  selector: '[zcmsMobileSidebarToggler]'
})
export class MobileSidebarToggleDirective {
  constructor() {
  }

  // Check if element has class
  private hasClass(target: any, elementClassName: string) {
    return new RegExp('(\\s|^)' + elementClassName + '(\\s|$)').test(target.className);
  }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    document.querySelector('body').classList.toggle('sidebar-show');
  }
}

/**
 * Allows the off-canvas sidebar to be closed via click.
 */
@Directive({
  selector: '[zcmsSidebarClose]'
})
export class SidebarOffCanvasCloseDirective {
  constructor() {
  }

  // Check if element has class
  private hasClass(target: any, elementClassName: string) {
    return new RegExp('(\\s|^)' + elementClassName + '(\\s|$)').test(target.className);
  }

  // Toggle element class
  private toggleClass(elem: any, elementClassName: string) {
    let newClass = ' ' + elem.className.replace(/[\t\r\n]/g, ' ') + ' ';
    if (this.hasClass(elem, elementClassName)) {
      while (newClass.indexOf(' ' + elementClassName + ' ') >= 0) {
        newClass = newClass.replace(' ' + elementClassName + ' ', ' ');
      }
      elem.className = newClass.replace(/^\s+|\s+$/g, '');
    } else {
      elem.className += ' ' + elementClassName;
    }
  }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();

    if (this.hasClass(document.querySelector('body'), 'sidebar-off-canvas')) {
      this.toggleClass(document.querySelector('body'), 'sidebar-opened');
    }
  }
}

@Directive({
  selector: '[zcmsBrandMinimizer]'
})
export class BrandMinimizeDirective {
  constructor() {
  }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    document.querySelector('body').classList.toggle('brand-minimized');
  }
}


/**
 * Allows the aside to be toggled via click.
 */
@Directive({
  selector: '[zcmsAsideMenuToggler]',
})
export class AsideToggleDirective implements OnInit {
  @Input('zcmsAsideMenuToggler') breakpoint: string;

  public bp;

  constructor() {
  }

  ngOnInit(): void {
    this.bp = this.breakpoint;
  }

  @HostListener('click', ['$event'])
  toggleOpen($event: any) {
    $event.preventDefault();
    let cssClass;
    this.bp ? cssClass = `aside-menu-${this.bp}-show` : cssClass = asideMenuCssClasses[0];
    ToggleClasses(cssClass, asideMenuCssClasses);
  }
}

const RemoveClasses = (NewClassNames) => {
  const MatchClasses = NewClassNames.map((Class) => document.querySelector('body').classList.contains(Class));
  return MatchClasses.indexOf(true) !== -1;
};

export const ToggleClasses = (Toggle, ClassNames) => {
  const Level = ClassNames.indexOf(Toggle);
  const NewClassNames = ClassNames.slice(0, Level + 1);

  if (RemoveClasses(NewClassNames)) {
    NewClassNames.map((Class) => document.querySelector('body').classList.remove(Class));
  } else {
    document.querySelector('body').classList.add(Toggle);
  }
};

// @ts-ignore
export const sidebarCssClasses: Array<string> = [
  'sidebar-show',
  'sidebar-sm-show',
  'sidebar-md-show',
  'sidebar-lg-show',
  'sidebar-xl-show'
];

// @ts-ignore
export const asideMenuCssClasses: Array<string> = [
  'aside-menu-show',
  'aside-menu-sm-show',
  'aside-menu-md-show',
  'aside-menu-lg-show',
  'aside-menu-xl-show'
];
