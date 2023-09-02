import {Component, OnInit} from '@angular/core';
import {UserDTO} from "../../../models/user-dto";
import {SocialMediaDTO} from "../../../models/socialmedia-dto";
import {Description} from "../../../models/description";
import {forkJoin} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../services/user.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AssignModel} from "../../../models/assign-model";

@Component({
  selector: 'app-user-update',
  templateUrl: './user-update.component.html',
  styleUrls: ['./user-update.component.scss']
})
export class UserUpdateComponent implements OnInit{
  isLoaded = false;
  frm: FormGroup;
  socialMedia!: SocialMediaDTO ;
  images!: string ;
  description!: Description ;
  userid = parseInt(<string>localStorage.getItem("forum_user_id"));
  selectedFile: File | null = null;
  selectedImageUrl: string | null = null;
  constructor(private userService: UserService, private router:Router,private formBuilder: FormBuilder) {
    this.frm = this.formBuilder.group({
      id: [this.userid, [Validators.required]],
      description: ["", [Validators.required]],
      instagramuser: ["", [Validators.required]],
      pinterestuser: ["", [Validators.required]],
      linkedinuser: ["", [Validators.required]],
      twitteruser: ["", [Validators.required]]
    })
  }

  ngOnInit(): void {
    const getSocialMedia$ = this.userService.getCustomerSocialMedia((this.userid));
    const getDescription$ = this.userService.getCustomerDescription((this.userid));
    const getImage$ = this.userService.getImage(this.userid);

    forkJoin({
      socialMedia: getSocialMedia$,
      description: getDescription$,
      images: getImage$
    }).subscribe(
      (results: {
        socialMedia: SocialMediaDTO,
        description: Description,
        images: Blob
      }) => {
        this.socialMedia = results.socialMedia;
        this.description = results.description;
        this.images = URL.createObjectURL(results.images);
        this.isLoaded = true;
      },
      error => {
        // Handle errors
      }
    );

  }

  onSubmit(event: Event) {
    this.userService.updateCustomer(
      {
        id: this.id?.value,
        description: this.descriptions?.value,
        twitteruser: this.twitteruser?.value,
        instagramuser: this.instagramuser?.value,
        pinterestuser: this.pinterestuser?.value,
        linkedinuser: this.linkedinuser?.value
      } as AssignModel)
      .subscribe((val) =>{
        if (val[0] === "Success"){
          this.uploadImage()
        }
      })
  }
  onFileSelected(event: any) {
    const file = event.target.files[0] as File;
    if (file) {
      this.selectedFile = file;

      // Set the selected image URL for preview
      const reader = new FileReader();
      reader.onload = (e) => {
        // @ts-ignore
        this.selectedImageUrl = e.target.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  uploadImage() {
    if (this.selectedFile) {
      this.userService.uploadImage(this.selectedFile, this.userid)
        .subscribe(
          (response) => {
            this.router.navigate([`profile/${this.userid}`])
          },
          (error) => {
            this.router.navigate([`profile/${this.userid}`])
          }
        );
    }else {
      this.router.navigate([`profile/${this.userid}`])
    }
  }
  onDelete() {
    this.userService.deleteCustomer(this.userid).subscribe(()=>{
      localStorage.removeItem("forum_user_id")
      localStorage.removeItem("forum_access_token")
      this.router.navigate([""])
    });
  }

  get id() {
    return this.frm.get('id');
  }
  get descriptions() {
    return this.frm.get('description');
  }
  get instagramuser() {
    return this.frm.get('instagramuser');
  }
  get pinterestuser() {
    return this.frm.get('pinterestuser');
  }
  get linkedinuser() {
    return this.frm.get('linkedinuser');
  }
  get twitteruser() {
    return this.frm.get('twitteruser');
  }
}
