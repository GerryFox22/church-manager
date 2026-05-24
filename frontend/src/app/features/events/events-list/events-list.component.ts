import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { EventsService } from '../../../core/services/events.service';
import { ChurchEvent } from '../../../shared/models/event.model';

@Component({
  selector: 'app-events-list',
  imports: [RouterLink, DatePipe],
  templateUrl: './events-list.component.html',
  styleUrl: './events-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EventsListComponent {
  private readonly eventsService = inject(EventsService);
  protected readonly authService = inject(AuthService);

  protected readonly items = signal<ChurchEvent[]>([]);
  protected readonly isLoading = signal(true);
  protected readonly errorMessage = signal<string | null>(null);

  constructor() {
    this.loadEvents();
  }

  protected deleteItem(id: number): void {
    if (!confirm('Delete this event?')) {
      return;
    }

    this.eventsService.delete(id).subscribe({
      next: () => this.loadEvents(),
      error: () => this.errorMessage.set('Unable to delete event.'),
    });
  }

  private loadEvents(): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.eventsService.getAll().subscribe({
      next: (items) => {
        this.items.set(items);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Unable to load events.');
        this.isLoading.set(false);
      },
    });
  }
}
