# NBT Console
A utility tool for Minecraft where you can create NBT files!

For now it doesn't support creating GZip or Deflate for other ocasions but, it's open source!

## It now supports:

- Reading/Overwriting/Deleting Bytes
- Some tags like `TAG_Double`, `TAG_Byte`, `TAG_List`, `TAG_Compound` and `TAG_End`.

## How to use?

For the tags, you need to type `[id]`

(`id` is the ID of the tag, check https://minecraft.wiki/w/NBT_Format, for more info).

Each Tag has its own number of parameters, all of them except `TAG_List` have a boolean as its final parameters (`isForList`), and what it does? Well, it removes the header of the TAGs for the `TAG_List` read correctly without having useless bytes as unknown data.

Let me show an example:

`[1]("the_name_goes_here", 127, false)`

#### Breakdown

`"the_name_goes_here"`: is obvious!

`127`: the value

`false`: the `isForList` bool, `true` for no header, `false` for header

After that, press Enter. If nothing happens, check for code errors in the console.

## Other

Although the code looks massive in amount of lines, it's super easy to understand!

I didn't metion other commands here, but you can always check the source for more info.
