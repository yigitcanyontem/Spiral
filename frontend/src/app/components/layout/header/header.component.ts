import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { UserDTO } from '../../../models/user-dto';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { forkJoin } from 'rxjs';
import { AuthenticationService } from '../../../services/authentication.service';
import { ConfirmationService, MenuItem } from 'primeng/api';
import { Menu } from 'primeng/menu';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  providers: [ConfirmationService],
})
export class HeaderComponent implements OnInit {
  userid = parseInt(<string>localStorage.getItem('forum_user_id'));
  input: string = '';
  user!: UserDTO;
  isLoaded = false;
  items: MenuItem[] | undefined;
  @ViewChild('menu', { static: false }) menu: Menu | undefined;

  @HostListener('window:scroll', ['$event'])
  onScroll(event: Event): void {
    if (this.menu && this.menu.visible) {
      this.menu.hide();
    }
  }

  constructor(
    private userService: UserService,
    private router: Router,
    private authService: AuthenticationService,
    private confirmationService: ConfirmationService,
  ) {}

  loggenInItems() {
    this.items = [
      {
        label: 'Welcome',
        items: [
          {
            label: 'Profile',
            icon: 'pi pi-user',
            command: () => {
              this.router.navigate(['/user/' + this.userid]);
            },
          },
          {
            label: 'Reviews',
            icon: 'fi fi-sr-note',
            command: () => {
              this.router.navigate(['/user/reviews/' + this.userid]);
            },
          },
          {
            label: 'Logout',
            icon: 'pi pi-sign-out',
            command: () => {
              this.confirmLogOut(event!);
            },
          },
        ],
      },
    ];
  }

  noUserItems() {
    this.items = [
      {
        label: 'Welcome',
        items: [
          {
            label: 'Sign Up',
            icon: 'fi fi-rr-user-add',
            command: () => {
              this.router.navigate(['/signup']);
            },
          },
          {
            label: 'Login',
            icon: 'pi pi-sign-in',
            command: () => {
              this.router.navigate(['/login']);
            },
          },
        ],
      },
    ];
  }

  onSearch() {
    let search = this.input;
    this.router.navigate(['/search/' + search]);
  }

  onLogout() {
    this.authService.onLogout().subscribe();
    window.location.reload();
  }

  ngOnInit(): void {
    if (!isNaN(this.userid)) {
      this.loggenInItems();

      const getUser$ = this.userService.getUser(this.userid);

      forkJoin({
        user: getUser$,
      }).subscribe(
        (results: { user: UserDTO }) => {
          this.user = results.user;
          this.isLoaded = true;
        },
        (error) => {
          // Handle errors
        },
      );
    } else {
      this.noUserItems();
    }
  }

  confirmLogOut(event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      header: 'Confirmation',
      message: 'Are you sure that you want to logout?',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.onLogout();
      },
      reject: () => {},
    });
  }

  protected readonly isNaN = isNaN;
}
