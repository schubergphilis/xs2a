import { Component, OnInit } from '@angular/core';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-help-page',
  templateUrl: './help-page.component.html',
  styleUrls: ['./pis-help-page.component.css']
})
export class PisHelpPageComponent implements OnInit {
  SWAGGER_URL = `${environment.xs2aServerUrl}/swagger-ui.html#`;

  constructor() { }

  ngOnInit() {
  }

}
