import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { User } from './user';
import { Basket } from './basket';
import { Need } from './need';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userUrl = 'http://localhost:2006/api';
  private favoritesUrl = 'http://localhost:2006/api/user/favorites'
  private currentUser: User | null = null;

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) { }

  // Method to sign up with a username
  signup(username: string): Observable<any> {
    return this.http.post(`${this.userUrl}/signup`, username, {
      headers: { 'Content-Type': 'application/json' },
    });
  }

  // Method to log in with a username
  login(username: string): Observable<any> {
    return this.http.post(`${this.userUrl}/login`, username, {
      headers: { 'Content-Type': 'application/json' },
    });
  }

  setCurrentUser(user: User | null): void{
    this.currentUser = user;
  }

  getCurrentUser(): Observable<User | null> {
    if (this.currentUser) {
      return of(this.currentUser);
    }
    return this.http.get<User>(`${this.userUrl}/user/home`).pipe(
      catchError(error => {
        console.error('Error fetching current user:', error);
        return of(null);
      })
    );
  }

  private handleError<T>(operation = 'operation', result?:T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    }
  }

  logout(): Observable<void> {
    const url = '${this.userUrl}/logout';
    return this.http.get<void>(url).pipe(
      tap(() => console.log('user logged out')),
      catchError(this.handleError<void>('logout'))
    );
  }

  getAllUsers(): Observable<User[]> {
    const url = '${this.userUrl}/users';
    return this.http.get<User[]>(url).pipe(
      tap(() => console.log('fetched all users')),
      catchError(this.handleError<User[]>('getAllUsers', []))
    );
  }

  updateUser(user: User): Observable<User> {
    const url = '${this.userUrl}/user/${user.userID}';
    return this.http.put<User>(url, user, this.httpOptions).pipe(
      tap(updateUser => console.log('updated user with id=${updatedUser.userID')),
      catchError(this.handleError<User>('updateUser'))
    );
  }

  getUserBasket(): Observable<Basket> {
    return this.http.get<Basket>(`${this.userUrl}/user/basket`).pipe(
      tap(() => console.log('fetched basket for user')),
      catchError(this.handleError<Basket>('getUserBasket'))
    );
  }

  addFavorite(need: Need): Observable<Need[]> {
    console.log("trying to add " + need.name + " to favorites.")
    return this.http.post<Need[]>(this.favoritesUrl, need, this.httpOptions);
  }

  getFavorites(): Observable<Need[]> {
    return this.http.get<Need[]>(this.favoritesUrl).pipe(
      tap(() => console.log('fetched favorites for user')),
      catchError(this.handleError<Need[]>('getFavorites'))
    );
  }

  removeFromFavorites(need: Need): Observable<Need[]> {
    return this.http.delete<Need[]>(`${this.favoritesUrl}/delete`, {
      headers: this.httpOptions.headers,
      body: need, // Pass the `Need` object as the body
    });
  }

}
