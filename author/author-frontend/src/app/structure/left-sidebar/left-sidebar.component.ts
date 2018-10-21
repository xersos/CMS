import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'zcms-left-sidebar',
  templateUrl: './left-sidebar.component.html',
  styleUrls: ['./left-sidebar.component.scss']
})
export class LeftSidebarComponent implements OnInit {

  public readonly navItems = [
    {
      name: 'Dashboard',
      url: '/dashboard',
      icon: 'icon-speedometer',
      // badge: {
      //   variant: 'info',
      //   text: 'NEW'
      // }
    },
    {
      title: true,
      name: 'Content'
    },
    {
      name: 'Sites',
      url: '/sites',
      icon: 'icon-book-open'
    },
    {
      name: 'Pages',
      url: '/pages',
      icon: 'icon-doc'
    },
    // {
    //   divider: true
    // },
    // {
    //   title: true,
    //   name: 'Extras',
    // },
    // {
    //   name: 'Pages',
    //   url: '/pages',
    //   icon: 'icon-star',
    //   children: [
    //     {
    //       name: 'Login',
    //       url: '/login',
    //       icon: 'icon-star'
    //     },
    //     {
    //       name: 'Register',
    //       url: '/register',
    //       icon: 'icon-star'
    //     },
    //     {
    //       name: 'Error 404',
    //       url: '/404',
    //       icon: 'icon-star'
    //     },
    //     {
    //       name: 'Error 500',
    //       url: '/500',
    //       icon: 'icon-star'
    //     }
    //   ]
    // }
  ];

  constructor() { }

  ngOnInit() {
  }

}
