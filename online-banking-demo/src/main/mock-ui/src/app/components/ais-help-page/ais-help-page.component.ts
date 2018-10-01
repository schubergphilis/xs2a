import { Component, OnInit } from '@angular/core';
import { AisService } from '../../service/ais.service';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-help-page',
  templateUrl: './ais-help-page.component.html',
  styleUrls: ['./ais-help-page.component.scss']
})
export class AisHelpPageComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router, private aisService: AisService) {
  }

  ngOnInit() {
  }
}
