import { CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  return (
    localStorage.getItem('forum_user_id') == null &&
    isNaN(parseInt(<string>localStorage.getItem('forum_user_id')))
  );
};
