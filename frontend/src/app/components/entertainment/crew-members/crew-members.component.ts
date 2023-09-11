import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EntertainmentService } from '../../../services/entertainment.service';
import { CrewMember } from '../../../models/crew-member';

@Component({
  selector: 'app-crew-members',
  templateUrl: './crew-members.component.html',
  styleUrls: ['./crew-members.component.scss'],
})
export class CrewMembersComponent implements OnInit {
  id = parseInt(<string>this.activatedRoute.snapshot.paramMap.get('id'));
  isLoaded = false;

  crewMember: CrewMember = {} as CrewMember;

  constructor(
    private activatedRoute: ActivatedRoute,
    private entertainmentService: EntertainmentService,
  ) {}

  ngOnInit(): void {
    this.entertainmentService.onGetCrew(this.id).subscribe((data) => {
      this.crewMember = data;
      this.isLoaded = true;
    });
  }
}
