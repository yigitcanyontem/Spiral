import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent {
  reportForm = new FormGroup({
    userName: new FormControl(`Yiğit Can Yöntem`, Validators.required),
    url: new FormControl(``, Validators.required),
    description: new FormControl(``, Validators.required),
  });
}
