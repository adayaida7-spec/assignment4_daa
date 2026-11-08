import csv
import statistics
from collections import defaultdict
from pathlib import Path

import matplotlib.pyplot as plt


CSV_PATH = Path("results/results.csv")
PLOT_DIR = Path("results/plots")
PLOT_DIR.mkdir(parents=True, exist_ok=True)

with CSV_PATH.open(newline="") as file:
    rows = list(csv.DictReader(file))


def grouped_median(algorithm, input_type, metric):
    values = defaultdict(list)

    for row in rows:
        if row["algorithm"] == algorithm and row["input"] == input_type:
            values[int(row["n"])].append(float(row[metric]))

    return {
        n: statistics.median(samples)
        for n, samples in sorted(values.items())
    }


def save_plot(filename, title, x_label, y_label, takeaway):
    plt.title(title)
    plt.xlabel(x_label)
    plt.ylabel(y_label)
    plt.grid(True, alpha=0.3)
    plt.legend()
    plt.figtext(0.5, 0.015, takeaway, ha="center", wrap=True, fontsize=9)
    plt.tight_layout(rect=(0, 0.08, 1, 1))
    plt.savefig(PLOT_DIR / filename, dpi=180)
    plt.close()

for algorithm in ("recursive", "table", "two_rows"):
    data = grouped_median(algorithm, "random", "time_ms")
    plt.plot(list(data), list(data.values()), marker="o", label=algorithm)

save_plot(
    "edit_distance_time.png",
    "Edit Distance: median runtime",
    "String length n (characters)",
    "Median runtime (milliseconds)",
    "Notice how the recursive version grows faster than the dynamic programming versions."
)

for algorithm in ("table", "two_rows"):
    data = grouped_median(algorithm, "random", "operations")
    plt.plot(list(data), list(data.values()), marker="o", label=algorithm)

save_plot(
    "edit_distance_operations.png",
    "Edit Distance: DP cell updates",
    "String length n (characters)",
    "Cell updates (count)",
    "Both DP versions update the same number of cells; two rows save memory."
)

for algorithm in ("naive", "kmp"):
    for input_type in ("random", "repeated_near_match"):
        data = grouped_median(algorithm, input_type, "operations")
        label = f"{algorithm}: {input_type}"
        plt.plot(list(data), list(data.values()), marker="o", label=label)

save_plot(
    "search_comparisons.png",
    "String Search: character comparisons",
    "Text length n (characters)",
    "Character comparisons (count)",
    "The repeated near-match makes Naive recheck long prefixes; KMP reuses prefix information."
)
for algorithm in ("naive", "kmp"):
    data = grouped_median(algorithm, "repeated_near_match", "time_ms")
    plt.plot(list(data), list(data.values()), marker="o", label=algorithm)

plt.xscale("log", base=2)
plt.yscale("log")

save_plot(
    "search_runtime_log.png",
    "String Search: near-match runtime (log-log)",
    "Text length n (characters, log scale)",
    "Median runtime (milliseconds, log scale)",
    "The logarithmic axes make it easier to compare runtime growth across input sizes."
)

print(f"Charts saved in {PLOT_DIR.resolve()}")