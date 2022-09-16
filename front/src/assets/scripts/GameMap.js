import { GameObject } from "./GameObject";
import { Wall } from "./Wall";
import { Snake } from "./Snake";

export class GameMap extends GameObject {
  constructor(ctx, parent) {
    super();

    this.ctx = ctx;
    this.parent = parent;
    this.unit = 0; // unit length

    this.rows = 13;
    this.cols = 14;

    this.walls = [];
    this.inner_walls_count = 20;

    this.snakes = [
      new Snake({ id: 0, color: "#4876EC", r: this.rows - 2, c: 1 }, this),
      new Snake({ id: 1, color: "#F94848", r: 1, c: this.cols - 2 }, this),
    ];
  }

  // Using flood fill method to check_connectivity
  check_connectivity(has_walls, sx, sy, tx, ty) {
    if (sx == tx && sy == ty) {
      return true;
    }
    has_walls[sx][sy] = true;

    let dx = [-1, 0, 1, 0],
      dy = [0, 1, 0, -1];

    for (let i = 0; i < 4; i++) {
      let x = sx + dx[i],
        y = sy + dy[i];
      if (
        !has_walls[x][y] &&
        this.check_connectivity(has_walls, x, y, tx, ty)
      ) {
        return true;
      }
    }

    return false;
  }

  create_walls() {
    const has_walls = [];

    for (let r = 0; r < this.rows; r++) {
      has_walls[r] = [];
      for (let c = 0; c < this.cols; c++) {
        has_walls[r][c] = false;
      }
    }

    // Add walls to the edged of the map
    for (let r = 0; r < this.rows; r++) {
      has_walls[r][0] = has_walls[r][this.cols - 1] = true;
    }

    for (let c = 0; c < this.cols; c++) {
      has_walls[0][c] = has_walls[this.rows - 1][c] = true;
    }

    // Create inner walls randomly
    for (let i = 0; i < this.inner_walls_count / 2; i++) {
      for (let j = 0; j < 1000; j++) {
        let r = parseInt(Math.random() * this.rows);
        let c = parseInt(Math.random() * this.cols);

        if (
          has_walls[r][c] ||
          has_walls[this.rows - 1 - r][this.cols - 1 - c]
        ) {
          continue;
        }

        if ((r == this.rows - 2 && c == 1) || (c == this.cols - 2 && r == 1)) {
          continue;
        }

        has_walls[r][c] = has_walls[this.rows - 1 - r][
          this.cols - 1 - c
        ] = true;
        break;
      }
    }

    // Deep copy has_walls
    const copy_has_walls = JSON.parse(JSON.stringify(has_walls));

    if (
      !this.check_connectivity(
        copy_has_walls,
        this.rows - 2,
        1,
        1,
        this.cols - 2
      )
    ) {
      return false;
    }

    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if (has_walls[r][c]) {
          this.walls.push(new Wall(r, c, this));
        }
      }
    }

    return true;
  }

  add_listening_events() {
    this.ctx.canvas.focus();

    const [snake0, snake1] = this.snakes;
    this.ctx.canvas.addEventListener("keydown", (e) => {
      if (e.key === "w") {
        snake0.set_direction(0);
      } else if (e.key === "d") {
        snake0.set_direction(1);
      } else if (e.key === "s") {
        snake0.set_direction(2);
      } else if (e.key === "a") {
        snake0.set_direction(3);
      } else if (e.key === "ArrowUp") {
        snake1.set_direction(0);
      } else if (e.key === "ArrowRight") {
        snake1.set_direction(1);
      } else if (e.key === "ArrowDown") {
        snake1.set_direction(2);
      } else if (e.key === "ArrowLeft") {
        snake1.set_direction(3);
      }
    });
  }

  start() {
    for (let i = 0; i < 1000; i++) {
      if (this.create_walls()) {
        break;
      }
    }

    this.add_listening_events();
  }

  update_size() {
    this.unit = parseInt(
      Math.min(
        this.parent.clientWidth / this.cols,
        this.parent.clientHeight / this.rows
      )
    );

    this.ctx.canvas.width = this.unit * this.cols;
    this.ctx.canvas.height = this.unit * this.rows;
  }

  // Referee method to check if both snakes are ready for the next turn.
  check_ready() {
    for (const snake of this.snakes) {
      if (snake.status !== "idle") {
        return false;
      }
      if (snake.direction === -1) {
        return false;
      }
    }
    return true;
  }

  next_step() {
    for (const snake of this.snakes) {
      snake.next_step();
    }
  }

  // Check if ther target cell is valid: no collision with snakes' bodies or the walls.
  check_valid(cell) {
    for (const wall of this.walls) {
      if (wall.r === cell.r && wall.c === cell.c) {
        return false;
      }
    }

    for (const snake of this.snakes) {
      let k = snake.cells.length;
      // If the tail is moving, no need to check the collision with this tail
      if (!snake.check_tail_increasing()) {
        k--;
      }

      for (let i = 0; i < k; i++) {
        if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) {
          return false;
        }
      }
    }
    return true;
  }

  update() {
    this.update_size();

    if (this.check_ready()) {
      this.next_step();
    }

    this.render();
  }

  render() {
    const color_even = "#AAD751",
      color_odd = "#A2D149";

    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if ((r + c) % 2 == 0) {
          this.ctx.fillStyle = color_even;
        } else {
          this.ctx.fillStyle = color_odd;
        }
        this.ctx.fillRect(c * this.unit, r * this.unit, this.unit, this.unit);
      }
    }
  }
}