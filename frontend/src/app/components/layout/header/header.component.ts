import {Component, HostListener, OnInit} from '@angular/core';
import {UserDTO} from "../../../models/user-dto";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../services/user.service";
import {forkJoin} from "rxjs";
import {SocialMediaDTO} from "../../../models/socialmedia-dto";
import {Description} from "../../../models/description";
import {Movie} from "../../../models/movie";
import {Show} from "../../../models/show";
import {Book} from "../../../models/book";
import {Game} from "../../../models/game";
import {Album} from "../../../models/album";
import {AuthenticationService} from "../../../services/authentication.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit{
  userid =  parseInt(<string>localStorage.getItem("forum_user_id"));
  input: string = "";
  user!: UserDTO ;
  images!: string ;
  isLoaded = false;

  constructor(private activatedRoute: ActivatedRoute,private userService: UserService, private router:Router, private authService:AuthenticationService) {

  }


  onSearch() {
    let search = this.input;
  }

  onLogout(){
    this.authService.onLogout().subscribe()
    window.location.reload()
  }

  ngOnInit(): void {
    if (!isNaN(this.userid)){
      const getUser$ = this.userService.getUser((this.userid));
      const getImage$ = this.userService.getImage(this.userid);

      forkJoin({
        user: getUser$,
        images: getImage$
      }).subscribe(
        (results: {
          user: UserDTO,
          images: Blob
        }) => {
          this.user = results.user;
          this.images = URL.createObjectURL(results.images);
          this.isLoaded = true;
        },
        error => {
          // Handle errors
        }
      );
    }
  }
}
