import {NgModule} from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import {PluginsComponent} from './plugins/plugins.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    PluginsComponent
  ]
})
export class ToolsModule {
}
