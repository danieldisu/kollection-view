# kollection-view

A RecyclerView wrapper with a few extra niceties:
- Use DiffUtils by default to calculate the differences between the old list and the new list, this allows the user
to work in a React style of programming, when you always send the full list of elements instead of keeping track of
the changes in the list.
- Easily allows to have different types of views.


## Artifacts

This library is only uploaded to the GitHub registry:

```
implementation("com.danieldisu:kollection-view:0.0.3")
```

```
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/danieldisu/kollection-view")
        credentials {
            username = github_packages_user
            password = github_packages_token
        }
    }
}
...
}
```


## How to use it

- Add the `KollectionView`to the layout.
- Call the configure method
- Call the update method with the list of elements that you want to display

```kotlin 
val kollectionView: KollectionView<SampleViewModel, SampleItemView> =
    findViewById(R.id.kollectionView)


kollectionView.configure(
    viewFactory = ::viewFactory,
    viewModelComparisonFunction = ::viewModelComparisonFunction,
    diffInMainThread = UITestUtils.isEspresso()
)

kollectionView.update(
    listOf(
        SampleViewModel(
            id = "id1",
            name = "name 1",
            points = "55"
        ),
        SampleViewModel(
            id = "id2",
            name = "name 2",
            points = "35"
        ),
        SampleViewModel(
            id = "id3",
            name = "name 3",
            points = "25"
        ),
        SampleViewModel(
            id = "id4",
            name = "name 4",
            points = "20"
        ),
        SampleViewModel(
            id = "id5",
            name = "name 5",
            points = "18"
        )
    )
)
```


## Key Classes

### KollectionItemView

This interface represents each view of the list, it *have to be implemented by a view*.

The bind function could be called several times with different items, so it must be deterministic.


```kotlin
class SampleItemView(context: Context) : KollectionItemView<SampleViewModel>, LinearLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.item_view_sample, this, true)
    }

    override fun bind(viewModel: SampleViewModel) {
        val nameView = findViewById<TextView>(R.id.nameView)
        val scoreView = findViewById<TextView>(R.id.scoreView)

        nameView.text = viewModel.name
        scoreView.text = viewModel.points
    }

}
```

### ViewModel

This could be any object, this object will represent every item in the list.

It's recommended that they are data classes or else you are implementing the equals method, so the diff function could work correctly.

```kotlin
data class SampleViewModel(
    val id: String,
    val name: String,
    val points: String
)
```

### ViewModelComparisonFunction

This function is passed to the configure method of the KollectionView, it will receive 2 elements and you should return a true if the 2 elements represent the same element.

Is important to note that this function is usually implemented by checking that the id is the same, not the whole item.

```kotlin
    private fun viewModelComparisonFunction(left: SampleViewModel, right: SampleViewModel): Boolean =
        left.id == right.id
```

Implementing the function like this allows the diff utils to know that the item is the same even if some fields of the item have changed. This will change the adapter calls for the `RecyclerView`.

(If the item is the same it will dispatch an `onChanged` vs `onRemoved` + `onInserted`)

### ViewFactory

The `ViewFactory` is a function that takes an int (the viewtype) and returns a `KollectionItemView`

The simplest implementation for a list with only one item type would be:

```kotlin
private fun viewFactory(viewType: Int): SampleItemView = SampleItemView(context = this)
```

More complicated collections could be implemented by returning a different `KollectionItemView`for each view type

### ViewTypeFactory

The `ViewTypeFactory` is a function that takes a ViewModel and returns an int that represents a `ViewType`. This should only be implemented when there is more than one `ViewType`.
