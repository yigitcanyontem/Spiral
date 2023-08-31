import { Component } from '@angular/core';

@Component({
  selector: 'app-all-users',
  templateUrl: './all-users.component.html',
  styleUrls: ['./all-users.component.scss']
})
export class AllUsersComponent {
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

  readonly columns = Object.keys(this.data[0]);
}
