import {Component, OnInit} from '@angular/core';
import {PaginatedCollection} from '../../shared/models/paginated.collection';
import {BaseComponent} from '../../base.component';
import {PluginService} from '../../shared/service/plugin.service';
import {takeUntil} from 'rxjs/operators';
import {Plugin} from '../../shared/models/entity/plugin';
import {EmitterService} from '../../shared/service/emitterService';

@Component({
  selector: 'zcms-plugins',
  templateUrl: './plugins.component.html',
  styleUrls: ['./plugins.component.scss']
})
export class PluginsComponent extends BaseComponent implements OnInit {

  private static readonly DELETE_CONFIRM_KEYS: string[] = [
    'COMPONENT.PLUGINS.DELETE.title',
    'COMPONENT.PLUGINS.DELETE.body',
    'COMPONENT.PLUGINS.DELETE.ok',
    'COMPONENT.PLUGINS.DELETE.close',
  ];

  public plugins: Plugin[] = [];

  public error: string = null;
  public success: string = null;

  constructor(private pluginService: PluginService) {
    super();

    EmitterService.of('pluginError')
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(error => this.error = error);
  }

  ngOnInit() {
    this.loading();
    this.pluginService.getPlugins()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((plugin: Plugin[]) => {
        this.plugins = plugin;
        this.doneLoading();
      });
  }

  public askForPluginDeletion(plugin: Plugin): boolean {

    return false;
  }

  public startPlugin(plugin: Plugin): boolean {
    this.loading();
    this.pluginService.startPlugin(plugin.id)
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((res: boolean) => {
        if (res) {
          plugin.state = 'STARTED';
        }
        this.doneLoading();
      });
    return false;
    return false;
  }

  public stopPlugin(plugin: Plugin): boolean {
    this.loading();
    this.pluginService.stopPlugin(plugin.id)
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((res: boolean) => {
        if (res) {
          plugin.state = 'STOPPED';
        }
        this.doneLoading();
      });
    return false;
  }
}
