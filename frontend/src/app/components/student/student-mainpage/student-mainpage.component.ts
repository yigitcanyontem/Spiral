import { Component } from '@angular/core';

@Component({
  selector: 'app-student-mainpage',
  templateUrl: './student-mainpage.component.html',
  styleUrls: ['./student-mainpage.component.scss']
})
export class StudentMainpageComponent {

  readonly data = [
    {
      "Internship Description": 'Software Development',
      "Start - End Date": '10/07/2023 - 18/08/2023',
      "Internship Duration - Week-Days": '30 gün, Haftada 5 gün',
      "Company, Department": 'Sarıtay Bilişim Anonim Şirketi,Yazılım',
      "Responsible Person, Phone": 'Sinan Melikoğlu sinanmelikoglu@saritay.com',
      "Internship Report": '1',
      "Internship Status": 'Staj bitti, henüz kurumca onaylanmadı',
    }
  ] as const;

  constructor() {
    console.log(this.data[0]["Company, Department"])
  }
}

