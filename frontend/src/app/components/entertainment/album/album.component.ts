import { Component, OnInit } from '@angular/core';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { ActivatedRoute } from '@angular/router';
import { EntertainmentService } from '../../../services/entertainment.service';
import { ReviewService } from '../../../services/review.service';
import { forkJoin } from 'rxjs';
import { Album } from '../../../models/album';
import { MessageService } from 'primeng/api';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  styleUrls: ['./album.component.scss'],
  providers: [MessageService],
})
export class AlbumComponent implements OnInit {
  userLogged!: boolean;
  favorited!: boolean;

  usersid = <string>localStorage.getItem('forum_user_id');
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  entertainmentType = EntertainmentType.ALBUM;
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'upvote';
  direction: string = 'ASC';
  rating = null;

  isLoaded = false;
  album!: Album;
  average!: number;

  error = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private entertainmentService: EntertainmentService,
    private reviewService: ReviewService,
    private userService: UserService,
    private messageService: MessageService,
  ) {}

  ngOnInit(): void {
    const getAlbum$ = this.entertainmentService.onGetAlbum(this.id);
    const getRating$ = this.reviewService.getAverageRatingByEntertainment(
      this.id,
      this.entertainmentType,
    );
    forkJoin({
      album: getAlbum$,
      average: getRating$,
    }).subscribe(
      (results: { album: Album; average: number }) => {
        this.album = results.album;
        this.average = results.average;
        this.checkUser();
        this.isLoaded = true;
      },
      (error) => {
        this.error = true;
        this.isLoaded = true;
      },
    );
  }

  onGetAverage() {
    this.reviewService
      .getAverageRatingByEntertainment(this.id, this.entertainmentType)
      .subscribe((results) => {
        this.average = results;
      });
  }

  checkUser() {
    this.userService
      .checkFavoritedAlbums(parseInt(this.usersid), this.id)
      .subscribe(
        (results) => {
          this.favorited = results;
        },
        (error) => {
          this.favorited = false;
        },
      );
    this.userLogged = this.usersid !== null;
  }

  addFavorite() {
    this.userService.saveFavAlbums(parseInt(this.usersid), this.id).subscribe(
      (results) => {
        this.favorited = true;
      },
      (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'An Error Occured.',
        });
      },
    );
  }

  deleteFavorite() {
    this.userService.deleteFavAlbums(parseInt(this.usersid), this.id).subscribe(
      (results) => {
        this.favorited = false;
      },
      (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'An Error Occured.',
        });
      },
    );
  }
}
